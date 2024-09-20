extern crate rand;
use std::{thread, time};
use std::io::{self, Write};
use std::fs::{self, OpenOptions};
use std::collections::HashSet;

trait Lyrics {
    fn bottles(self, primes:&Vec<u32>, n:u32) -> Self;
    fn take(self) -> Self;
    fn wall(self) -> Self;
    fn mid(self) -> Self;
    fn end(self) -> Self;
}

impl Lyrics for String {
    fn bottles(mut self, primes:&Vec<u32>, n:u32) -> String {
        self.push_str(&format!("{0} line{1} of text", factor(&n, primes), if n == 1 {""} else {"s"}));
        self
    }

    fn take(mut self) -> String {
        self.push_str("Print it out, stand up and shout, ");
        self
    }

    fn wall(mut self) -> String {
        self.push_str(" on the screen");
        self
    }

    fn mid(mut self) -> String {
        self.push_str(", ");
        self
    }

    fn end(mut self) -> String {
        self.push_str(".\n");
        self
    }
}

fn factor(num:&u32, primes:&Vec<u32>) -> String {
    let mut n = *num; 
    let mut s = String::from("(");
    while n > 1 {
        for p in primes {
            if n % p == 0 {
                s.push_str(&p.to_string());
                n /= p;
                if !(n == 1) {
                    s.push_str("*");
                }
                break;
            }
        }
    }
    s.push_str(")");
    s
}

fn generate_primes(n:u32) -> Vec<u32> {
    let mut primes = Vec::new();
    let mut sieve = HashSet::new();
    for i in 2..=n {
        if !sieve.contains(&i) {
            primes.push(i);
            let mut inc = i;
            while inc <= n {
                sieve.insert(inc);
                inc += i;
            }
        }
    }
    primes
}

fn generate_lyric(n:u32, inc:u32, primes:&Vec<u32>) -> String {
    String::from("").bottles(primes, n).wall().mid().bottles(primes, n).end().take().bottles(primes, n+inc).wall().end()
}

fn main() {

    // get random number to increment by
    let rand_num:u32 = rand::random();
    let inc = (rand_num % 10) + 1;

    // print prompt
    print!("How many lines: ");
    io::stdout().flush().unwrap();

    // reading from stdin is harder than we expected...
    // help from this forum: https://users.rust-lang.org/t/how-to-read-an-integer-from-stdin/57538
    let mut input = String::new();
    io::stdin().read_line(&mut input).expect("Failed to read line!");
    let n:u32 = input.trim().parse().expect("Input is not an integer!");

    // generate primes up to n
    let primes = generate_primes(n + inc);

    // create copies to pass to thread
    let primes_copy = primes.clone();
    let n_copy = n.clone();
    let inc_copy = inc.clone();

    // create separate thread
    thread::spawn(move || {

        // delete file if it exists
        _ = fs::remove_file("out.txt");

        // open file in append
        let mut out_file = OpenOptions::new().write(true).create(true).append(true).open("out.txt").unwrap();

        let mut i = 1;
        while i < n_copy {
            writeln!(out_file, "{}", generate_lyric(i, inc_copy, &primes_copy)).unwrap();
            i += inc_copy;
        }

        eprintln!("File finished!");
    });

    println!("\n----------\n");

    // loop enough times
    let mut i = 1;
    while i < n {
        
        println!("{}", generate_lyric(i, inc, &primes));

        //sleep for one second between every iteration
        thread::sleep(time::Duration::new(1, 0));
        i += inc;
    }

    eprintln!("Screen finished!");
}