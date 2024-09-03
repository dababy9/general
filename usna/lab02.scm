;;; SI 413 Lab 2
;;; MIDN Caleb Walker

;;; 1
(define (get-rate cur)
  (cond ((eqv? cur 'usd) 1)
        ((eqv? cur 'euro) 0.76)
        ((eqv? cur 'yen) 98.18)
        ((eqv? cur 'won) 1109.85)))
(define (to-usdollar amt cur)
  (/ amt (get-rate cur)))

;;; 2
(define (from-usdollar amt cur)
  (* amt (get-rate cur)))

;;; 3
(define (convert amt fromCur toCur)
  (from-usdollar (to-usdollar amt fromCur) toCur))

;;; 4
(define (squares i j)
  (if (> i j)
      '()
      (cons (* i i) (squares (+ i 1) j))))

;;; 5
(define (longer? L1 L2)
  (cond ((null? L1) #f)
        ((null? L2) #t)
        (else (longer? (cdr L1) (cdr L2)))))

;;; 6
(define (sum-cash L)
  (if (null? L)
      0
      (+ (sum-cash (cdr L)) (to-usdollar (caar L) (cadar L)))))

;;; 7
(define (length L)
  (if (null? L)
      0
      (+ 1 (length (cdr L)))))
(define (average L n)
  (if (null? L)
      0
      (+ (/ (car L) n) (average (cdr L) n))))
(define (compute-num L u)
  (if (null? L)
      0
      (+ (* (- (car L) u) (- (car L) u)) (compute-num (cdr L) u))))
(define (std-dev L)
  (let ((n (length L)))
    (let ((u (average L n)))
      (sqrt (/ (compute-num L u) (- n 1))))))

;;; 8
(define (contains? L n)
  (if (null? L)
      #f
      (if (= (car L) n)
          #t
          (contains? (cdr L) n))))
(define (uniquefy L)
  (if (null? L)
      '()
      (let ((num-set (uniquefy (cdr L))))
        (if (contains? num-set (car L))
            (cons (car L) num-set)
            num-set))))