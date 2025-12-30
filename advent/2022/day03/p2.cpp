#include <iostream>
#include <fstream>
using namespace std;

unsigned long long getMask(string s){
	unsigned long long mask = 0;
	for(char c : s)
		mask |= 1ULL << ((c >= 'a') ? c - 'a' : c - 'A' + 26);
	return mask;
}

int main(){
	ifstream fin("input.txt");
	int total = 0;
	string s1, s2, s3;
	while(getline(fin, s1)){
		if(s1.empty()) continue;
    	getline(fin, s2);
    	getline(fin, s3);
		unsigned long long mask1 = getMask(s1), mask2 = getMask(s2);
		for(int i = 0; i < s3.size(); i++){
			int id = (s3[i] >= 'a') ? s3[i] - 'a' : s3[i] - 'A' + 26;
			if(mask1 & (1ULL << id) && mask2 & (1ULL << id)){
				total += id + 1;
				break;
			}
		}
	}
	cout << total << endl;
	return 0;
}