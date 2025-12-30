#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>
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
	vector<Packet> packets;
	while(getline(fin, s)){
		if(s.empty()) continue;
		int cursor = 0;
		packets.push_back(createPacket(s, cursor));
	}
	Packet div1 {-1, vector<Packet>{Packet {-1, vector<Packet>{Packet {2}}}}}, div2 {-1, vector<Packet>{Packet {-1, vector<Packet>{Packet {6}}}}};
	packets.push_back(div1);
	packets.push_back(div2);
	sort(packets.begin(), packets.end(), [](const Packet& a, const Packet& b){ return compare(a, b) < 0; });
	int index1 = 0, index2 = 0;
	for(int i = 0; i < packets.size(); i++){
		if(compare(div1, packets[i]) == 0) index1 = i + 1;
		if(compare(div2, packets[i]) == 0) index2 = i + 1;
	}
	cout << index1 * index2 << endl;
	return 0;
}