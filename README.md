# About
TODO: List us all with images and positions

# TODO Gradle
Output jar after running jar task will be located in build/libs/

## build.gradle / gradle.properties
This is the gradle configuration file. Modify this file to add dependencies to your project. In
 general you should only modify the depedencies section of this file, however there are a few
  modification you will need to make when you begin the project  
  - jaCoCo
    - jaCoCo is a JAva COde COverage checker that enforces testing. By default the rules are 25
    % line coverage and 25% branch coverage, but if you would like to be more successful you
     should raise these numbers higher to enforce team members to write more tests. Simply modify
      the `minimum` values to enforce stricter tests (but do not change to below .25, as that is
       the required minimum for this class)