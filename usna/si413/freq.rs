// CALEB WALKER and SHAWN BERRIOS
// SI413 Project Part 2
//-------------------------------------



//------ NOTE ------//

//------------------//



// libraries to use
use std::collections::HashMap;
use std::{env, process};
use std::io::{self, Read, Write};
use std::fmt::Display;
use std::fs::File;



// main function
fn main() {

    // read in filename
    let filename = env::args().nth(1)

        // give the following message if user doesn't give a filename
        .ok_or("Please supply a file name")

        // print error if anything goes wrong
        .unwrap_or_else(|e| exit_err(e, 1));

    let mut buf = String::new();
    let mut count = HashMap::new();

    File::open(&filename)
        .unwrap_or_else(|e| exit_err(e, 2))
        .read_to_string(&mut buf)
        .unwrap_or_else(|e| exit_err(e, 3));


    for c in buf.chars() {
        *count.entry(c).or_insert(0) += 1;
    }

    println!("Number of occurences per character");
    for (ch, count) in &count {
        println!("{:?}: {}", ch, count);
    }
}



// function for handling/printing errors and exiting with status codes
fn exit_err<T>(msg: T, code: i32) -> ! where T: Display {
    writeln!(&mut io::stderr(), "{}", msg).expect("Could not write to stderr");
    process::exit(code)
}