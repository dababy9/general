extern crate rand;
use std::{io, thread, time};
use std::io::Write;
use std::collections::HashSet;

trait Lyrics {
    fn bottles(&self, primes: &Vec<u32>) -> Self;
    fn take(&self, inc:u32) -> Self;
    fn wall(&self) -> Self;
    fn mid(&self) -> Self;
    fn end(&self);
}

impl Lyrics for u32 {
    fn bottles(&self, primes: &Vec<u32>) -> u32 {
        print!("{0} line{1} of text", factor(self, primes), if *self == 1 {""} else {"s"});
        *self
    }

    fn take(&self, inc:u32) -> u32 {
        print!("Print it out, stand up and shout, ");
        *self + inc
    }

    fn wall(&self) -> u32 {
        print!(" on the screen");
        *self
    }

    fn mid(&self) -> u32 {
        print!(", ");
        *self
    }

    fn end(&self) {
        println!(".");
    }
}

fn factor(num: &u32, primes: &Vec<u32>) -> String {
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
    for i in 2..n+1 {
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

fn main() {

    // get random number to increment by
    let rand_num: u32 = rand::random();
    let inc = (rand_num % 10) + 1;

    // print prompt
    print!("How many lines: ");
    io::stdout().flush().unwrap();

    // reading from stdin is harder than we expected...
    // help from this forum: https://users.rust-lang.org/t/how-to-read-an-integer-from-stdin/57538
    let mut input = String::new();
    io::stdin().read_line(&mut input).expect("Failed to read line!");
    let n: u32 = input.trim().parse().expect("Input is not an integer!");

    // generate primes up to n
    let primes = generate_primes(n + inc);

    println!("\n----------\n");

    // loop enough times
    let mut i = 1;
    while i <= n {
        i.bottles(&primes).wall().mid().bottles(&primes).end();
        i.take(inc).bottles(&primes).wall().end();
        println!();

        //sleep for one second between every iteration
        thread::sleep(time::Duration::new(1, 0));
        i += inc;
    }
}