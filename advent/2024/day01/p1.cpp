#include <iostream>
#include <fstream>
#include <sstream>
#include <list>
using namespace std;

int main(){
    list<int> l;
    ifstream f("input.txt");
    string str;

    while(getline(f, str)){
        stringstream s(str);
    }

    f.close();
    return 0;
}