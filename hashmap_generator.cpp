#include <stdio.h>
#include <iostream>
#include <fstream>

using namespace :: std;

int main()
{
    ofstream outfile("output.txt");

    int file1, file2;
    file1 = 10;

    for(int i=0; i<file1; i++){
        outfile << i << ".jpg" << endl;
    }
}
