;;; SI 413 Lab 3
;;; MIDN Caleb Walker

;;; 1
(define (min-sin x . vals)
  (min-sin-helper vals x))
(define (min-sin-helper L min)
  (if (null? L)
      min
      (if (< (sin (car L)) (sin min))
          (min-sin-helper (cdr L) (car L))
          (min-sin-helper (cdr L) min))))

;;; 2
(define (group k . L)
  (reverse (group-helper k L '())))
(define (group-helper k L R)
  (if (null? L)
      R
      (group-helper k (removeK k L) (cons (firstK k L) R))))
(define (removeK k L)
  (if (or (null? L) (= k 0))
      L
      (removeK (- k 1) (cdr L))))
(define (firstK k L)
  (if (or (null? L) (= k 0))
      '()
      (cons (car L) (firstK (- k 1) (cdr L)))))

;;; 3
(define (base b)
  (lambda (n)
    (reverse (digits n b '()))))
(define (digits n b R)
  (if (= (quotient n b) 0)
      (cons (remainder n b) R)
      (digits (quotient n b) b (cons (remainder n b) R))))

;;; 4
(define (make-cXr op . ops)
  (lambda (L)
    (make-cXr-helper (reverse (cons op ops)) L)))
(define (make-cXr-helper ops L)
  (cond ((null? ops) L)
        ((equal? (car ops) 'a) (make-cXr-helper (cdr ops) (car L)))
        ((equal? (car ops) 'd) (make-cXr-helper (cdr ops) (cdr L)))))

;;; 5
(define (make-stack)
  (let ((S '()))
    (lambda (message . L)
      (cond ((equal? message 'size)
             (length S))
            ((equal? message 'pop)
             (let ((x (car S)))
               (set! S (cdr S))
               x))
            ((equal? message 'push)
             (set! S (add-items S L)))))))
(define (add-items S L)
  (if (null? L)
      S
      (cons (car L) (add-items S (cdr L)))))

;;; 6, 7, 8, 9
(define (make-set)
  (let ((S '()))
    (lambda (message . L)
      (cond ((equal? message 'get)
             S)
            ((equal? message 'set!)
             (set! S L))
            ((equal? message 'size)
             (length S))
            ((equal? message 'insert!)
             (insert S L))
            ((equal? message 'contains?)
             (contains? S L))))))
(define (insert S L)
  (cond ((null? L)
         S)
        ((contains? S (car L))
         (insert S (cdr L)))
        (else
         (insert (insert-sorted '() (car L) S) (cdr L)))))
(define (insert-sorted R x S)
  (if (> x (car S))
      (insert-sorted (append R (list (car S))) x (cdr S))
      (append R (cons x S))))
(define (contains? L x)
  (cond ((null? L)
         #f)
        ((= (car L) x)
         #t)
        (else (contains? (cdr L) x))))
        