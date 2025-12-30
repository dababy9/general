#include <iostream>
#include <fstream>
#include <vector>
#include <sstream>
#include <algorithm>
using namespace std;

struct Monkey {
	vector<long long> items;
	int inspections = 0, tID, fID, div, opNum;
	bool opAdd;
};

int lastNum(ifstream& fin){
	string line;
	getline(fin, line);
	return stoi(line.substr(line.find_last_of(' ') + 1));
}

int main(){
	ifstream fin("input.txt");
	vector<Monkey> monkeys;
	string line;
	int lcm = 1;
    while(getline(fin, line)){
        if(line.empty()) continue;
        Monkey m;
        getline(fin, line);
        stringstream ss(line.substr(line.find(':') + 1));
        long long x;
        char comma;
        while (ss >> x) {
            m.items.push_back(x);
            ss >> comma;
        }
        getline(fin, line);
        m.opAdd = line.find('+') != string::npos;
        string rhs = line.substr(line.find_last_of(' ') + 1);
        m.opNum = (rhs == "old") ? -1 : stoi(rhs);
        m.div = lastNum(fin);
		lcm *= m.div;
        m.tID = lastNum(fin);
		m.fID = lastNum(fin);
        monkeys.push_back(m);
    }
	for(int n = 0; n < 10000; n++)
		for(Monkey& m : monkeys){
			for(long long item : m.items){
				if(m.opAdd) item += (m.opNum < 0 ? item : m.opNum);
				else item *= (m.opNum < 0 ? item : m.opNum);
				item %= lcm;
				if(item % m.div == 0) monkeys[m.tID].items.push_back(item);
				else monkeys[m.fID].items.push_back(item);
				m.inspections++;
			}
			m.items.clear();
		}
	vector<long long> inspections;
	for(Monkey m : monkeys)
		inspections.push_back(m.inspections);
	sort(inspections.rbegin(), inspections.rend());
	cout << inspections[0] * inspections[1] << endl;
	return 0;
}