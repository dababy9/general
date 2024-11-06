// CALEB WALKER and SHAWN BERRIOS
// SI413 Project Part 2
//-------------------------------------



// libraries to use
use splay::SplayMap;
use std::collections::BinaryHeap;
use std::{env, process};
use std::io::{self, Read, Write};
use std::fmt::Display;
use std::fs::File;



// struct and implementations for heap
// derive ordering traits for struct (because we must implement Ord, PartialOrd, Eq, and PartialEq to use in heap), similar to Comparable and compareTo() in Java
// deriving traits means Rust automatically generates the functions in a naive way, but it is exactly what we want for the ordering
// For Ord and PartialOrd, Rust compares fields in lexicographic order, so 'c' first, and then 'w'.
// For Eq and PartialEq, Rust just makes sure all fields are equal
#[derive(PartialEq, Eq, PartialOrd, Ord)]
struct WordCount {
    c: i32,
    w: String,
}



// main function
fn main() {

    // read in filename
    let filename = env::args().nth(1)

        // give the following message if user doesn't give a filename
        .ok_or("Please supply a file name")

        // print error if anything goes wrong
        .unwrap_or_else(|e| exit_err(e, 1));

    // create buffer to store file contents
    let mut buf = String::new();

    // create hashmap to store counts of words
    let mut tree = SplayMap::new();

    // open file (using filename)
    File::open(&filename)

        // print error if file does not exist
        .unwrap_or_else(|e| exit_err(e, 2))

        // read file to buffer
        .read_to_string(&mut buf)

        // print error if read fails
        .unwrap_or_else(|e| exit_err(e, 3));

    // loop through all tokens separated by whitespace
    for word in buf.split_whitespace() {

        // set all letters to lowercase to make count more robust
        let lower = word.to_lowercase();

        // trim any extra punctuation characters
        let trimmed = lower.trim_matches(&['.', '?', '!', '"', '\'', ',', '(', ')', '[', ']', '{', '}', ';', ':', '~', '-']).to_string();
        
        // if key is already in the tree, increment the value by 1
        // get_mut returns an OPTION, which can be either Some or None
        // rust does not allow for arbitrary values (like null)
        // this is the workaround used, and how we check if get_mut returned something
        if let Some(n) = tree.get_mut(&trimmed) {
            *n += 1;

        // otherwise, add the key to the tree
        } else {
            tree.insert(trimmed, 1);
        }
    }

    // remove any weird 'words' that are only punctuation (i. e. '-----------------')
    tree.remove("");

    // create vector of WordCounts from tree
    let elements : Vec<WordCount> = tree.into_iter()

        // iterator from tree returns just a string and integer, so we must map that to a new WordCount objects
        .map(|(string, integer)| WordCount {c: integer, w: string})

        // collect into vector
        .collect();

    // create heap from vector
    let mut top = BinaryHeap::from(elements);

    // print top 50 elements from heap
    for i in 0..50 {
        let item = top.pop().unwrap();
        println!("{}. {:?}: {}", i+1, item.w, item.c);
    }
}



// function for handling/printing errors and exiting with status codes
// this is from the original code that printed letter frequencies
fn exit_err<T>(msg: T, code: i32) -> ! where T: Display {
    writeln!(&mut io::stderr(), "{}", msg).expect("Could not write to stderr");
    process::exit(code)
}