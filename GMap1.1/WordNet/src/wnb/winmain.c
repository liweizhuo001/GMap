#include <windows.h>
#include <stdio.h>

static Id = "$Id: winmain.c,v 1.2 2005/02/01 17:46:22 wn Rel $";

extern int main(int, char**, char**);

int WINAPI
WinMain(HINSTANCE current, HINSTANCE prev, LPSTR cmdline, int showcmd)
{
	int argc = 0;
	char *argv[2];

	argv[argc++] = "wnb";
    return main(argc, argv, NULL);
}
