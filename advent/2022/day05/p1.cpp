#include <iostream>
#include <fstream>
#include <vector>
using namespace std;

int main(){
	ifstream fin("input.txt");
	string s;
	vector<string> v(9);
	while(true){
		getline(fin, s);
		if(s[1] == '1') break;
		for(int i = 1, j = 0; i < s.size(); i += 4, j++)
			if(s[i] != ' ') v[j].insert(v[j].begin(), s[i]);
	}
	getline(fin, s);
	int amt, from, to;
	while(fin >> s >> amt >> s >> from >> s >> to){
		for(int i = 0; i < amt; i++){
			v[to - 1] += v[from - 1].back();
			v[from - 1].pop_back();
		}
	}
	for(string s : v)
		cout << s.back();
	cout << endl;
	return 0;
}