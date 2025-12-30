#include <iostream>
#include <fstream>
using namespace std;

int main(){
	int total = 0, l1, h1, l2, h2;
	char dump;
	ifstream fin("input.txt");
	while(fin >> l1 >> dump >> h1 >> dump >> l2 >> dump >> h2)
		if((h1 >= l2 && l1 <= l2) || (h2 >= l1 && l2 <= l1)) total++;
	cout << total << endl;
	return 0;
}