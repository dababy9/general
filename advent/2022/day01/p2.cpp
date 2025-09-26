#include <iostream>
#include <fstream>
using namespace std;

int main(){
    ifstream fin("input.txt");
    int m1 = 0, m2 = 0, m3 = 0, elf = 0;
    string s;
    while(fin){
        getline(fin, s);
        if(s == ""){
            if(elf > m1) m1 = elf;
            else if(elf > m2) m2 = elf;
            else if(elf > m3) m3 = elf;
            elf = 0;
        } else elf += stoi(s);
    }
    cout << (m1 + m2 + m3) << endl;
    return 0;
}