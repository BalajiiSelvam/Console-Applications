#include <stdio.h>
#define MAX 50
int main(){
    char str[MAX];
    scanf("%s",str);
    int len = 0;

    for(int i=0; str[i]; i++){
        len++;
    }
    int arr[len];
    int index = 0;

    for(int i=0; str[i]; i++){
        char ch = str[i];
        if(ch=='{' || ch=='[' || ch=='('){
            arr[index++] = ch;
        }
        else if(ch=='}' && arr[index-1]=='{' || 
                ch==']' && arr[index-1]=='[' || 
                ch==')' && arr[index-1]=='('){
            arr[index--];
        }
    }
    if(index==0) printf("Valid\n\n");
    else printf("Invalid\n\n");

}