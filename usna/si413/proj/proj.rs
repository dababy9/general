trait Lyrics {
    fn bottles(&self, bp: str, wp: str) -> Self;
    fn take(&self) -> Self;
    fn wall(&self) -> Self;
    fn mid(&self) -> Self;
    fn end(&self);
}

impl Lyrics for u32 {
    fn bottles(&self) -> u32 {
        print!("{0}{1}{2}{3}", self, if *self == 1 {""} else {"s"});
        *self
    }

    fn take(&self) -> u32 {
        print!("Take one down and pass it around, ");
        *self + 1
    }

    fn wall(&self) -> u32 {
        print!(" on the wall");
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

fn part_b(){
    bottlePhrase = "line";
    wallPhrase = "of text on the screen, "
    for i in 1..100 {
        i.bottles().wall().mid().bottles().end();
        i.take().bottles().wall().end();
        println!();
    }
}

fn main() {
    part_b();
}