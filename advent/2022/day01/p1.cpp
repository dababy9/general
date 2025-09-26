#include <iostream>
#include <fstream>
using namespace std;

int main(){
    ifstream fin("input.txt");
    int max = 0, elf = 0;
    string s;
    while(fin){
        getline(fin, s);
        if(s == ""){
            if(elf > max) max = elf;
            elf = 0;
        } else elf += stoi(s);
    }
    cout << max << endl;
    return 0;
}