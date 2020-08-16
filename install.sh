echo "Downloading main JAR..." &&
wget -q "https://github.com/theapache64/boil/releases/latest/download/boil.main.jar" -O "boil.main.jar" --show-progress &&
# cp /home/theapache64/Documents/projects/boil/boil.main.jar boil.main.jar &&

echo "Moving files to ~/.boil" &&

mkdir -p ~/.boil &&
mv boil.main.jar ~/.boil/boil.main.jar &&

echo "Installing..." &&
echo "alias boil='java -jar ~/.boil/boil.main.jar'" >> ~/.bashrc &&

echo "Done"
