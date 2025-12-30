#include <iostream>
#include <fstream>
#include <bit>
using namespace std;

int main(){
	string s;
	ifstream fin("input.txt");
	fin >> s;
	int i;
	for(i = 13; i < s.length(); i++){
		unsigned long mask = 0;
		for(int j = i - 13; j <= i; j++)
			mask |= 1UL << s[j] - 'a';
		if(__builtin_popcountl(mask) == 14) break;
	}
	cout << (i + 1) << endl;
	return 0;
}