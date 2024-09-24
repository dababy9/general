#!/usr/bin/env racket
#lang racket

;;; Predict and Follow sets calculator.
;;; SI 413 Fall 2023
;;;
;;; To use this program, run it as
;;;   racket predfol.scm
;;; then type in your grammar rules like this:
;;;   nt -> nt other TOKEN something ELSE
;;; followed by Ctrl-D when you are done.
;;; (Or, you can redirect in from a file.)
;;; To specify an epsilon production, you can write
;;; "epsilon" or "ε" or just leave the right-hand side blank.

(require racket/format)

; splits a list according to the given match function.
; matcher should take a list and return 0 for no match,
; or else the length of the match at the beginning of the list.
; Returned is a list of lists for what appears between matches.
; maxmatch is the maximum list size to return (0 for no max)
; include-empties? indicates whether empty lists should be returned or omitted.
(define (split lst matcher maxlen include-empties?)
  (define (add-token token splits-accum)
    (if (or include-empties?
            (pair? token))
      (cons token splits-accum)
      splits-accum))
  (define (split-helper lst token-accum splits-accum max-remaining)
    (cond [(= 1 max-remaining)
           (reverse (add-token lst splits-accum))]
          [(null? lst)
           (reverse (add-token (reverse token-accum) splits-accum))]
          [else (let ([match-len (matcher lst)])
                  (if (zero? match-len)
                    (split-helper (cdr lst)
                                  (cons (car lst) token-accum)
                                  splits-accum
                                  max-remaining)
                    (split-helper (list-tail lst match-len)
                                  '()
                                  (add-token (reverse token-accum) splits-accum)
                                  (sub1 max-remaining))))]))
  (split-helper lst '() '() maxlen))

; matcher for split based on whitespace characters
(define (whitespace-matcher lst)
  (whitespace-matcher-helper lst 0))
(define (whitespace-matcher-helper lst count)
  (if (or (null? lst)
          (not (char-whitespace? (car lst))))
    count
    (whitespace-matcher-helper (cdr lst) (add1 count))))

; matcher for -> or →
(define (arrow-matcher lst)
  (cond [(null? lst) 0]
        [(char=? (car lst) #\→) 1]
        [(null? (cdr lst)) 0]
        [(and (char=? (car lst) #\-)
              (char=? (cadr lst) #\>))
         2]
        [else 0]))

; compares the length of the given list to n based on the comparison
; function comp.
; Example: (compare-len '(1 2 3 4) <= 5) produces #t
; The point is to avoid calling length, faster for very large lists.
(define (compare-len lst comp n)
  (cond [(null? lst)
         (comp 0 n)]
        [(zero? n)
         (comp 1 0)]
        [else
         (compare-len (cdr lst) comp (sub1 n))]))

; creates a matcher for split based on string pattern
(define (make-matcher pattern)
  (let ([patlist (string->list pattern)]
        [patlen (string-length pattern)])
    (lambda (lst)
      (if (and (compare-len lst >= patlen)
               (andmap char=?
                       patlist
                       (take lst patlen)))
        patlen
        0))))

(define comment-matcher (make-matcher "#"))
(define pipe-matcher (make-matcher "|"))

; takes a list of characters and returns a list of symbols, split according to whitespace.
; any symbol like 'eps or 'epsilon or 'ε is removed.
(define (to-symbols aloc)
  (filter
    (lambda (sym)
      (not (memv sym '(eps epsilon Eps Epsilon EPS EPSILON ε))))
    (map string->symbol
         (map list->string
              (split aloc whitespace-matcher 0 #f)))))

; adds production rules with the given nonterminal symbol to the accumulating list
(define (add-prods nt prods rules accum)
  (cond [(null? rules)
         (reverse (cons (cons nt prods)
                        accum))]
        [(eqv? nt (caar rules))
         (append (reverse accum)
                 (cons (cons nt (append (cdar rules) prods))
                       (cdr rules)))]
        [else
         (add-prods nt
                    prods
                    (cdr rules)
                    (cons (car rules) accum))]))

; reads lines from the given port to parse a grammer in EBNF form.
; returns a list of (nonterminal (production rule rhs) (production rule ...)) sub-lists
(define (parse-grammar port)
  (parse-grammar-helper port '()))
(define (parse-grammar-helper port sofar)
  (let ([line (read-line port)])
    (if (eof-object? line)
      sofar
      (let ([production (split (car (split (string->list line) comment-matcher 2 #t))
                               arrow-matcher
                               2
                               #t)])
        (if (null? (cdr production))
          (begin ;(assert (andmap char-whitespace? (car production)))
                 (parse-grammar-helper port sofar))
          (let ([lhs (to-symbols (car production))]
                [rhs (map to-symbols
                          (split (cadr production) pipe-matcher 0 #t))])
            ;(assert (compare-len lhs = 1))
            (parse-grammar-helper
              port
              (add-prods (car lhs) rhs sofar '()))))))))

; proc must be a function of one argument.
; keeps calling (proc arg) until the result is the same as arg (a fixed point),
; and then this arg is returned.
(define (fixed-point proc initial)
  (let ([next (proc initial)])
    (if (equal? initial next)
      initial
      (fixed-point proc next))))

; adds each element of new to lst at the end if not already present
(define (incorporate new lst)
  (if (null? new)
    lst
    (incorporate (cdr new)
                 (incorporate-helper (car new) lst '()))))
(define (incorporate-helper elem lst sofar)
  (cond [(null? lst)
         (reverse (cons elem sofar))]
        [(eqv? elem (car lst))
         (append (reverse sofar) lst)]
        [else (incorporate-helper elem (cdr lst) (cons (car lst) sofar))]))

; sorts one partial list and removes duplicates, according to a given sorted list
(define (sort-according unsorted sorted)
  (filter (lambda (x) (memv x unsorted))
          sorted))

; extracts all token names from the grammar, in the order they appear
; nts should be a list of nonterminals
(define (get-tokens grammar nts)
  (incorporate (append (filter (lambda (sym) (not (memv sym nts)))
                               (apply append (apply append (map cdr grammar))))
                       (list '$))
               '()))

; computes the EPS set for this grammar, the list of
; nonterminals which can produce empty strings.
(define (get-eps grammar)
  (fixed-point
    (lambda (eps)
      (map car
           (filter
             (lambda (rules)
               (ormap (lambda (rhs)
                        (andmap (lambda (sym) (memv sym eps))
                                rhs))
                      (cdr rules)))
             grammar)))
    '()))

; Gets the relations between first and follow sets of nonterminals.
; These are basically the edges in the relations graph.
; e.g. a relation '((follow B) (first A)) means any token in the
; first set of A should also be in the follow set of B.
(define (get-rels grammar eps nts)
  (define (get-rels-helper rules rels-sofar)
    (if (null? rules)
        rels-sofar
        (get-rels-helper (cdr rules)
                         (add-rhses (caar rules) (cdar rules) rels-sofar))))
  (define (add-rhses nt rhses rels-sofar)
    (if (null? rhses)
        rels-sofar
        (add-rhses nt
                   (cdr rhses)
                   (add-rhs nt
                            (car rhses)
                            (list (list 'first nt))
                            rels-sofar))))
  (define (add-rhs nt rhs add-to rels-sofar)
    (if (null? rhs)
        (append (map (lambda (x) (list x (list 'follow nt)))
                     (filter (lambda (x) (symbol=? (car x) 'follow))
                             add-to))
                rels-sofar)
        (add-rhs nt
                 (cdr rhs)
                 (let ([folnode (list 'follow (car rhs))])
                   (cond [(memv (car rhs) eps)
                          (cons folnode add-to)]
                         [(memv (car rhs) nts)
                          (list folnode)]
                         [else '()]))
                 (append (map (lambda (node)
                                (list node (list 'first (car rhs))))
                              add-to)
                         rels-sofar))))
  (get-rels-helper
    grammar
    (if (memv '$ (cadar grammar))
        '()
        (list (list (list 'follow (caar grammar))
                    (list 'first '$))))))

; removes duplicates from a list
(define (uniqueify lst)
  (uniqueify-helper lst '()))
(define (uniqueify-helper lst accum)
  (cond [(null? lst)
         (reverse accum)]
        [(member (car lst) accum)
         (uniqueify-helper (cdr lst) accum)]
        [else (uniqueify-helper (cdr lst)
                                (cons (car lst) accum))]))

; organizes edges into an adjacency list graph
(define (edges->graph edges)
  (eg-helper edges '()))
(define (eg-helper edges sofar)
  (if (null? edges)
      sofar
      (let ([test-first
             (lambda (true-false)
               (lambda (edge)
                 (eq? (equal? (caar edges) (car edge))
                      true-false)))])
        (eg-helper
          (filter (test-first #f)
                  edges)
          (cons (uniqueify (cons (caar edges)
                                 (map cadr (filter (test-first #t)
                                                   edges))))
                sofar)))))

; topological sort of a directed graph.
; returned is a list of lists of equivalent nodes, in topo sort order.
(define (topo-sort graph)
  (define (dfs-from-all nodes open lclosed)
    (if (null? nodes)
        lclosed
        (dfs-from-all (cdr nodes)
                      open
                      (dfs (car nodes) open lclosed))))
  (define (dfs node open lclosed)
    (cond [(ormap (lambda (lst) (member node lst))
                  (cdr lclosed))
           lclosed]
          [(member node open)
           (let ([loop (member node (reverse open))])
             (if (> (length loop) (car lclosed))
                 (cons loop (cdr lclosed))
                 lclosed))]
          [else
           (let ([newlc (let ([nbrs (assoc node graph)])
                          (if nbrs
                              (dfs-from-all (cdr nbrs) (cons node open) lclosed)
                              lclosed))])
             (if (and (pair? (car newlc))
                      (equal? node (caar newlc)))
                 (cons '() newlc)
                 (cons (car newlc)
                       (cons (list node)
                             (cdr newlc)))))]))
  (reverse (cdr (dfs-from-all (map car graph) '() '(())))))

; computes a list of all first and follow sets, unsorted.
(define (get-first-follow grammar nts tokens eps)
  (let ([graph (edges->graph (get-rels grammar eps nts))])
    (define (process node-groups sofar)
      (cond [(null? node-groups)
             sofar]
            [(and (symbol=? (caaar node-groups) 'first)
                  (memv (cadaar node-groups) tokens))
             ;(assert (null? (cdar node-groups)))
             (process (cdr node-groups)
                      (cons (list (caar node-groups) (cadaar node-groups))
                            sofar))]
            [else
             (let ([toks (sort-according
                           (apply append
                                  (map (lambda (nbr)
                                         (cdr (assoc nbr sofar)))
                                       (uniqueify (apply append
                                                         (map (lambda (node)
                                                                (let ([x (assoc node graph)])
                                                                  (if x (cdr x) '())))
                                                              (car node-groups))))))
                           tokens)])
               (process (cdr node-groups)
                        (append (map (lambda (node)
                                       (cons node toks))
                                     (car node-groups))
                                sofar)))]))
    (process (topo-sort graph) '())))

; ff should be output from get-first-follow
; type should be 'first or 'follow
(define (extract-ff ff type nts)
  (define (helper syms sofar)
    (if (null? syms)
        sofar
        (helper
          (cdr syms)
          (cons (let ([found (assoc (list type (car syms))
                                    ff)])
                  (if found
                      (cons (car syms)
                            (cdr found))
                      (list (car syms))))
                sofar))))
  (reverse (helper nts '())))

(define (get-predict grammar tokens eps first follow)
  (define (first-for sym)
    (if (memv sym tokens)
        (list sym)
        (cdr (assv sym first))))
  (define (predict-for nt rhs sofar)
    (if (null? rhs)
        (append (cdr (assv nt follow))
                sofar)
        (let ([sofar (append (first-for (car rhs))
                             sofar)])
          (if (memv (car rhs) eps)
              (predict-for nt (cdr rhs) sofar)
              sofar))))
  (map (lambda (rule)
         (list rule
               (sort-according (predict-for (car rule) (cdr rule) '())
                               tokens)))
       (apply append (map (lambda (grule)
                            (map (lambda (rhs) (cons (car grule) rhs))
                                 (cdr grule)))
                          grammar))))

; makes a big string from a list of strings by inserting the glue between everything
; alos must not be an empty string
(define (join alos glue)
  (cond [(null? alos) ""]
        [(null? (cdr alos))
         (car alos)]
        [else (string-append (car alos)
                             glue
                             (join (cdr alos) glue))]))

; like map, but will go according to the longer of the two argument lists
(define (map-longest fun args1 args2)
  (cond [(null? args1) args2]
        [(null? args2) args1]
        [else (cons (fun (car args1) (car args2))
                    (map-longest fun (cdr args1) (cdr args2)))]))

(define (max-widths lolos)
  (max-widths-helper lolos '()))
(define (max-widths-helper lolos sofar)
  (if (null? lolos)
      sofar
      (max-widths-helper (cdr lolos)
                         (map-longest max
                                      sofar
                                      (map string-length (car lolos))))))

(define (print-table alol)
  (let* ([widths (max-widths alol)])
    (header-line widths)
    (body-lines alol widths)
    (header-line widths)))
(define (header-line widths)
  (display ":-")
  (display (join (map (lambda (w) (make-string w #\-))
                      widths)
                 "-:-"))
  (display "-:")
  (newline))
(define (body-lines alol widths)
  (unless (null? alol)
    (printf ": ~a :~n"
            (join (map (lambda (s w) (~a s #:min-width w))
                       (car alol)
                       widths)
                  " : "))
    (body-lines (cdr alol) widths)))

(define (print-ff ffsets)
  (print-table
    (map (lambda (lst)
           (let ([los (map symbol->string lst)])
             (list (car los)
                   (join (cdr los) " "))))
         ffsets)))

; makes string from list of symbols with spaces in between
(define (rhs->string rhs)
  (if (null? rhs)
      "ε"
      (join (map symbol->string rhs) " ")))
(define (rule->string rule)
  (string-append
    (symbol->string (car rule))
    " → "
    (join (map rhs->string (cdr rule)) " | ")))
(define (print-predict alop)
  (print-table
    (map (lambda (pair)
           (list
             (rule->string (cons (caar pair)
                                 (list (cdar pair))))
             (join (map symbol->string (cadr pair)) " ")))
         alop)))

(define (process port)
  (let* ([grammar (parse-grammar port)]
         [nts (map car grammar)]
         [tokens (get-tokens grammar nts)]
         [eps (get-eps grammar)]
         [fflist (get-first-follow grammar nts tokens eps)]
         [first (extract-ff fflist 'first nts)]
         [follow (extract-ff fflist 'follow nts)]
         [predict (get-predict grammar tokens eps first follow)])
    (printf "      TOKENS: ~a\n" (join (map symbol->string tokens) " "))
    (printf "NONTERMINALS: ~a\n" (join (map symbol->string nts) " "))
    (printf "         EPS: ~a\n" (join (map symbol->string eps) " "))
    (newline)
    (printf "FIRST:\n")
    (print-ff first)
    (newline)
    (printf "FOLLOW:\n")
    (print-ff follow)
    (newline)
    (printf "PREDICT:\n")
    (print-predict predict)
    ))

(printf "Enter grammar rules below, followed by Ctrl-D~n")
(printf "Each rule should have the form:~n")
(printf "  nonterminal -> soment someothernt SOMETOKEN SOMEOTHERTOKEN~n")
(printf "Also supported: alternation symbol |, \"epsilon\" or \"ε\" for epsilon productions, comments with #~n")
(process (current-input-port))
