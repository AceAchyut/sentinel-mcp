#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// A simple, high-performance log scanner
int main(int argc, char *argv[]) {
    if (argc < 2) {
        printf("{\"error\": \"No file provided\"}\n");
        return 1;
    }

    char *filepath = argv[1];
    FILE *file = fopen(filepath, "r");

    if (!file) {
        printf("{\"error\": \"File not found or access denied\"}\n");
        return 1;
    }

    char line[1024];
    int error_count = 0;
    int warning_count = 0;
    int line_count = 0;

    // Fast scan loop
    while (fgets(line, sizeof(line), file)) {
        line_count++;
        if (strstr(line, "ERROR")) error_count++;
        if (strstr(line, "WARN")) warning_count++;
    }

    fclose(file);

    // Output JSON for the Java Server to read
    printf("{\"status\": \"success\", \"scanned_lines\": %d, \"errors\": %d, \"warnings\": %d, \"engine\": \"C-Native\"}\n", 
           line_count, error_count, warning_count);

    return 0;
}