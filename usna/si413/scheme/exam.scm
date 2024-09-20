;;; Caleb Walker
;;; SI413 Practicum

;;; Problem 1
(define (fold A B)
  (if (null? A)
      '()
      (cons (car A) (cons (car B) (fold (cdr A) (cdr B))))))

;;; Problem 2
(define (total-length lol)
  (apply + (map length lol)))

;;; Problem 3
(define (sublist L i j)
  (cond ((= j 0)
         (cons (car L) '()))
        ((= i 0)
         (cons (car L) (sublist (cdr L) i (- j 1))))
        (else
         (sublist (cdr L) (- i 1) (- j 1)))))

;;; Problem 4
(define (pir p . L)
  (let ((nums (filter (lambda (x) (< x p)) L)))
    (if (null? nums)
        'over
        (apply max nums))))

;;; Problem 5
(define (hotel c)
  (let ((o 0))
    (lambda (m)
      (cond ((eqv? m 'checkin!)
             (if (< o c)
                 (begin (set! o (+ o 1))
                        o)
                 'novacancy))
            ((eqv? m 'vacancies) (- c o))))))

;;; Problem 6
(define (reverse-tail L)
  (reverse-helper L '()))
(define (reverse-helper L R)
  (if (null? L)
      R
      (reverse-helper (cdr L) (cons (car L) R))))