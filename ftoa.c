

#include<stdio.h>
void flot(char* p, float x)
{
  int n,i=0,k=0;
  n=(int)x;
  while(n>0)
  {
    x/=10;
    n=(int)x;
    i++;
 }
 *(p+i) = '.';
 x *= 10;
 n = (int)x;
 x = x-n;
 while(n>0)
 {
   if(k == i)
        k++;
   *(p+k)=48+n;
   x *= 10;
   n = (int)x;
   x = x-n;
   k++;
 }
 *(p+k) = '\0';
}

int main()
{
  float x;
  char a[20]={};
  char* p=a;
  printf("Enter the float value.");
  scanf("%f",&x);
  flot(p,x);
  printf("The value=%s",p);
  getchar();
  return 0;
}

