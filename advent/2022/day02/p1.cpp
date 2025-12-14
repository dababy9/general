#include <fstream>
#include <iostream>
using namespace std;

int main(){
    ifstream fin("input.txt");
    char aChar, bChar;
    int total = 0;
    while (fin >> aChar >> bChar){
        int a = aChar - 'A', b = bChar - 'X';
        total += b + 1 + ((b - a) % 3 + 4) % 3 * 3;
    }
    cout << total << endl;
    return 0;
}