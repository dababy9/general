extern crate rand;
//use rand::Rng;

trait Lyrics {
    fn bottles(&self) -> Self;
    fn take(&self) -> Self;
    fn wall(&self) -> Self;
    fn mid(&self) -> Self;
    fn end(&self);
}

impl Lyrics for u32 {
    fn bottles(&self) -> u32 {
        print!("{0} line{1} of text", self, if *self == 1 {""} else {"s"});
        *self
    }

    fn take(&self) -> u32 {
        print!("Print it out, stand up and shout, ");
        *self + 1
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

fn print_lyric(i:u32) {
    i.bottles().wall().mid().bottles().end();
    i.take().bottles().wall().end();
    println!();
}

fn part_b() {
    for i in 1..100 {
        print_lyric(i);
    }
}

fn part_c() {
    let rand_num: u32 = rand::random();
    let inc = (rand_num % 10) + 1;
    let mut i = 0;
    while i <= 500 {
        print_lyric(i);
        i += inc;
    }
}

fn main() {
    //part_b();
    part_c();
}