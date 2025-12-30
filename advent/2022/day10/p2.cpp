#include <iostream>
#include <fstream>
using namespace std;

void runCycle(int& cyc, int val){
	if(cyc >= val - 1 && cyc <= val + 1) cout << '#';
	else cout << ' ';
	cyc++;
	if(cyc % 40 == 0){
		cout << endl;
		cyc = 0;
	}
}

int main(){
	ifstream fin("input.txt");
	int cyc = 0, val = 1, add;
	string s;
	while(fin >> s){
		runCycle(cyc, val);
		if(s != "noop"){
			runCycle(cyc, val);
			fin >> add;
			val += add;
		}
	}
	return 0;
}