case class CalculatorResult(result: Double = 0) {
  def reset = copy(result = 0)
  def add(value: Double) = copy(result = this.result + value)
  def subtract(value: Double) = copy(result = this.result - value)
  def divide(value: Double) = copy(result = this.result / value)
  def multiply(value: Double) = copy(result = this.result * value)
}
