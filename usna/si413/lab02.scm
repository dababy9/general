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
            num-set
            (cons (car L) num-set)))))

;;; 9
(define (test-sin x)
  (let ((e (+ (* (sin x) (sin x)) 1)))
    (+ (/ 1 e) (- (sqrt e) (* e e)))))

;;; 10
(define (dist f1 i1 f2 i2)
  (let ((L1 (+ i1 (* f1 12)))
        (L2 (+ i2 (* f2 12))))
    (abs (- L1 L2))))

;;; 11
(define (fd-at g n)
  (- (g (+ n 1)) (g n)))

;;; Given helper function (returns list of integers from 1 to n)
(define (range a b)
  (if (> a b)
      '()
      (cons a (range (+ a 1) b))))

;;; 12
(define (sqrt-prod n)
  (apply * (map sqrt (range 1 n))))

;;; 13
(define (special-nums n)
  (filter (lambda (x) (and (= 0 (modulo x 3))
                           (integer? (sqrt x))))
          (range 1 n)))

;;; 14
(define P1 '((3 5) (9 2) (11 6) (8 8) (4 6)))
(define (repeat x k)
  (if (= k 0)
      '()
      (cons x (repeat x (- k 1)))))
(define (translate-point p d)
  (map + p d))
(define (generate-lists d k)
  (if (null? d)
      (repeat '() k)
      (map cons (repeat (car d) k) (generate-lists (cdr d) k))))
(define (translate p d)
  (map translate-point (generate-lists d (length p)) p))