package gov.ca.cwds.jenkins.common

class ProjectTypesDeterminer {
  public static final String LINT_CONFIGS_RUBY = '.ruby-version'
  public static final String LINT_CONFIGS_JAVASCRIPT = 'package.json'
  public static final String LINT_CONFIGS_JAVA = 'build.gradle'
  def script

  ProjectTypesDeterminer(script) {
    this.script = script
  }

  def determineProjectTypes(directory) {
    script.echo "the directoy is ${directory}"
    def projectTypes = []
    if (isJavaProject(directory)) {
      projectTypes.add(ProjectTypes.JAVA)
    }
    if (isJavascriptProject(directory)) {
      projectTypes.add(ProjectTypes.JAVASCRIPT)
    }
    if (isRubyProject(directory)) {
      projectTypes.add(ProjectTypes.RUBY)
    }

    projectTypes
  }

  private isJavaProject(directory) {
    def filename = "${directory}/${LINT_CONFIGS_JAVA}"
    fileExists(filename)
  }

  private isJavascriptProject(directory) {
    def filename = "${directory}/${LINT_CONFIGS_JAVASCRIPT}"
    fileExists(filename)
  }

  private isRubyProject(directory) {
    def filename = "${directory}/${LINT_CONFIGS_RUBY}"
    fileExists(filename)
  }

  private fileExists(filename) {
    script.sh(script: "ls -al ${filename}", returnStatus: true) == 0
  }
}
