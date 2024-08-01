#!/bin/bash

# Define the destination directory
DEST_DIR="/tmp/"

# Create the destination directory if it doesn't exist
mkdir -p "$DEST_DIR"

# Find all directories named "fonts" and copy their contents to the destination
find . -type d -name "fonts" -print0 | while IFS= read -r -d '' dir; do
    echo "Copying fonts from $dir to $DEST_DIR"
    cp -r "$dir"/* "$DEST_DIR"
done



DEST_EXTRACTED="/usr/share/fonts/custom/"

mkdir -p "$DEST_EXTRACTED"
for f in "$DEST_DIR"/*.zip; do
   base_name=$(basename "$f" .zip)


   echo $f
   echo "BASENAME:::: " $base_name
#   unzip -o "$f" -d "./"
   unzip -o "$f" -d "/usr/share/fonts/custom/${base_name}"
   mkdir "/usr/share/fonts/truetype/${base_name}/"
   echo "TEST:" "/usr/share/fonts/truetype/${base_name}/"
   cp  "/usr/share/fonts/custom/$base_name"/*.ttf "/usr/share/fonts/truetype/$base_name/"
done



# Check if any fonts were copied
if [ "$(ls -A "$DEST_DIR")" ]; then
    echo "Fonts copied successfully. Updating font cache..."
    
    # Update the font cache
    fc-cache -fv
else
    echo "No fonts found. Skipping font cache update."
fi

