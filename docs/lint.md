# lint

## Usage

```groovy
  lint()
  lint(gradleRunTime)
```

* *gradleRunTime* Is required for java based projects

## Examples

```groovy
   stage('Linting Stage') {
      lint()
   }
```

```groovy
   stage('Linting Stage') {
      def rtGradle = Artifactory.newGradleBuild()
      lint(rtGradle)
   }
```

## Conventions
Linting will run on 3 different types of projects.

### Ruby
The existence of **.rubocop.yml** in the root directoy of the project will initiate a rubocop run.
The command run is ```rubocop```

### Javascript

The existence of **.eslintrc** in the root directoy **OR** the existence of **eslintConfig** in package.json will initiate a run of eslint.
The command run is ```npm run lint```

### Java
The existence of **build.gradle** in the root directoy will initiate a run of sonarqube.
The command run is

```groovy
withSonarQubeEnv('Core-SonarQube') {
  buildInfo = rtGradle.run buildFile: 'build.gradle', switches: '--info', tasks: 'sonarqube'
}
```

