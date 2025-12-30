#include <fstream>
#include <iostream>
using namespace std;

int main(){
    ifstream fin("input.txt");
    char aChar, bChar;
    int total = 0;
    while (fin >> aChar >> bChar){
        int a = aChar - 'A', b = bChar - 'X';
        total += b * 3 + (a + b + 2) % 3 + 1;
    }
    cout << total << endl;
    return 0;
}