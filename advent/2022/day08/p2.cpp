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
	int best = 0;
	for(int i = 1; i < rows-1; i++)
		for(int j = 1; j < cols-1; j++){
			int score = 1, dist = 0;
			for(int pos = i+1; pos < rows; pos++){
				dist++;
				if(grid[pos][j] >= grid[i][j]) break;
			}
			score *= dist;
			dist = 0;
			for(int pos = i-1; pos >= 0; pos--){
				dist++;
				if(grid[pos][j] >= grid[i][j]) break;
			}
			score *= dist;
			dist = 0;
			for(int pos = j+1; pos < cols; pos++){
				dist++;
				if(grid[i][pos] >= grid[i][j]) break;
			}
			score *= dist;
			dist = 0;
			for(int pos = j-1; pos >= 0; pos--){
				dist++;
				if(grid[i][pos] >= grid[i][j]) break;
			}
			score *= dist;
			if(score > best) best = score;
		}
	cout << best << endl;
	return 0;
}