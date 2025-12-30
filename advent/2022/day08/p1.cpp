#include <iostream>
#include <fstream>
#include <vector>
using namespace std;

int main(){
	ifstream fin("input.txt");
	vector<string> input;
	string s;
	while(getline(fin, s))
		input.push_back(s);
	int rows = input.size(), cols = input[0].size();
	int grid[rows][cols];
	for(int i = 0; i < rows; i++)
		for(int j = 0; j < input[i].size(); j++)
			grid[i][j] = input[i][j] - '0';
	bool visible[rows][cols] = {};
	for(int i = 0; i < rows; i++){
		int maxHeight = -1, pos = -1;
		while(maxHeight < 9 && ++pos < cols)
			if(grid[i][pos] > maxHeight){
				visible[i][pos] = true;
				maxHeight = grid[i][pos];
			}
		maxHeight = -1, pos = cols;
		while(maxHeight < 9 && --pos >= 0)
			if(grid[i][pos] > maxHeight){
				visible[i][pos] = true;
				maxHeight = grid[i][pos];
			}
	}
	for(int i = 0; i < cols; i++){
		int maxHeight = -1, pos = -1;
		while(maxHeight < 9 && ++pos < rows)
			if(grid[pos][i] > maxHeight){
				visible[pos][i] = true;
				maxHeight = grid[pos][i];
			}
		maxHeight = -1, pos = rows;
		while(maxHeight < 9 && --pos >= 0)
			if(grid[pos][i] > maxHeight){
				visible[pos][i] = true;
				maxHeight = grid[pos][i];
			}
	}
	int total = 0;
	for(int i = 0; i < rows; i++)
		for(int j = 0; j < cols; j++)
			if(visible[i][j]) total++;
	cout << total << endl;
	return 0;
}