import liqp.Template

// Parse the Liquid Template template
def template = Template.parse("the {{name}} is this")

// Create a binding
def binding = [name: "John Doe"]

// Render the Liquid Template template with the binding
def renderedOutput = template.render(binding)

// Get the rendered output
println renderedOutput
