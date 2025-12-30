#include <iostream>
#include <fstream>
#include <vector>
using namespace std;

struct Packet {
	int val = -1;
	vector<Packet> list;
};

Packet createPacket(const string& s, int& index) {
    Packet p;
    if (s[index] == '['){
        index++;
        while (s[index] != ']'){
            p.list.push_back(createPacket(s, index));
            if(s[index] == ',') index++;
        }
        index++;
    } else {
        p.val = 0;
        while(isdigit(s[index]))
			p.val = p.val * 10 + (s[index++] - '0');
    }
    return p;
}

int compare(const Packet& p1, const Packet& p2){
	if(p1.val < 0 && p2.val < 0){
		for(int i = 0; i < p1.list.size() && i < p2.list.size(); i++){
			int result = compare(p1.list[i], p2.list[i]);
			if(result != 0) return result;
		}
		return p1.list.size() - p2.list.size();
	}
	else if(p1.val < 0) return compare(p1, Packet {-1, {p2}});
	else if(p2.val < 0) return compare(Packet {-1, {p1}}, p2);
	else return p1.val - p2.val;
}

int main(){
	ifstream fin("input.txt");
	string s;
	int total = 0;
	for(int i = 1; getline(fin, s); i++){
		int cursor = 0;
		Packet p1 = createPacket(s, cursor);
		getline(fin, s);
		cursor = 0;
		Packet p2 = createPacket(s, cursor);
		getline(fin, s);
		if(compare(p1, p2) < 0) total += i;
	}
	cout << total << endl;
	return 0;
}