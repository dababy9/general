#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>
using namespace std;

struct Node {
	string name;
	int size;
	Node* parent;
	vector<Node*> children;
};

int computeSize(vector<int>& sizes, Node* n){
	for(Node* c : n->children)
		n->size += computeSize(sizes, c);
	sizes.push_back(n->size);
	return n->size;
}

int main(){
	ifstream fin("input.txt");
	Node* root = new Node {"", 0, NULL, {}};
	Node* curr = root;
	string s;
	getline(fin, s);
	fin >> s;
	while(fin >> s){
		if(s == "ls"){
			string l;
			while(fin >> l && l != "$"){
				fin >> s;
				if(l == "dir") curr->children.push_back(new Node {s, 0, curr, {}});
				else curr->size += stoi(l);
			}
		} else {
			fin >> s;
			if(s == "..") curr = curr->parent;
			else
				for(Node* n : curr->children)
					if(n->name == s){
						curr = n;
						break;
					}
			fin >> s;
		}
	}
	vector<int> sizes;
	computeSize(sizes, root);
	sort(sizes.begin(), sizes.end());
	int target = sizes.back() - 40000000;
	for(int size : sizes)
		if(size > target){
			cout << size << endl;
			break;
		}
	return 0;
}