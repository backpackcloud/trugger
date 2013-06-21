dist=$1
shift
formats=$@
for format in ${formats[@]}
do
  pandoc -o "$dist/trugger.$format" --highlight-style=pygments -s -S \
  $(find guides -type f | sort)
done
