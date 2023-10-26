static void main(String[] args) {
  println """Hello world!
con salto de linea
"""
  def m = "lo que sea"
  println(m)
  for (i in 0..<10) {
     print(i)
  }
  def arr = ['a', 'bec', 'other']
  println(arr)
  def map = [keyA: 40, keyB: 60]
  println(map)

  def plus = {
    a,b ->
      y = a+ b
      println(y)
  }
  plus(50, 12)
  def file = new File("C:\\Users\\maxne\\Downloads\\OM AddMissedReferencesV1.sql")
  println(file.text)
}

