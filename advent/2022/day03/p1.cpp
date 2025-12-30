#include <iostream>
#include <fstream>
using namespace std;

int main(){
	ifstream fin("input.txt");
	int total = 0;
	string s;
	while(getline(fin, s)){
		if(s.empty()) continue;
		unsigned long long mask = 0;
		int length = s.size();
		for(int i = 0; i < length / 2; i++)
			mask |= 1ULL << ((s[i] >= 'a') ? s[i] - 'a' : s[i] - 'A' + 26);
		for(int i = length / 2; i < length; i++){
			int id = (s[i] >= 'a') ? s[i] - 'a' : s[i] - 'A' + 26;
			if(mask & (1ULL << id)){
				total += id + 1;
				break;
			}
		}
	}
	cout << total << endl;
	return 0;
}