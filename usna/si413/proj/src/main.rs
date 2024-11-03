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
struct WordCount {
    c: i32,
    w: String,
}

impl Ord for WordCount {
    fn cmp(&self, other: &Self) -> std::cmp::Ordering {
        self.count.cmp(&other.count);
    }
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
        if let Some(n) = tree.get_mut(&trimmed) {
            *n += 1;

        // otherwise, add the key to the tree
        } else {
            tree.insert(trimmed, 1);
        }
    }

    // remove any weird 'words' that are only punctuation (i. e. '-----------------')
    tree.remove("");

    // create heap for top 50 elements
    let mut top = BinaryHeap::new();

    // loop through all mappings in the splay tree
    for (word, count) in tree.into_iter() {

        // if heap length is less than 50, just add the item
        if top.len() < 50 {
            top.push(WordCount {c: count, w: word});
        }
        println!("{:?}: {}", ch, count);
    }
}



// function for handling/printing errors and exiting with status codes
fn exit_err<T>(msg: T, code: i32) -> ! where T: Display {
    writeln!(&mut io::stderr(), "{}", msg).expect("Could not write to stderr");
    process::exit(code)
}