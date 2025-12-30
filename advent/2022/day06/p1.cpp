#include <iostream>
#include <fstream>
using namespace std;

int main(){
	string s;
	ifstream fin("input.txt");
	fin >> s;
	int i;
	for(i = 3; i < s.length(); i++)
		if(s[i] != s[i-3] && s[i] != s[i-2] && s[i] != s[i-1] && s[i-1] != s[i-3] && s[i-1] != s[i-2] && s[i-2] != s[i-3])
			break;
	cout << (i + 1) << endl;
	return 0;
}