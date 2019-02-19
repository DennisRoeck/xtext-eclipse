pipeline {
  agent {
    kubernetes {
      label 'build-test-pod'
      defaultContainer 'jnlp'
      yaml '''
        apiVersion: v1
        kind: Pod
        spec:
          containers:
          - name: container
            image: eclipsecbi/debian-gtk3-metacity
            tty: true
            command:
              - cat
          - name: jnlp
            image: 'eclipsecbi/jenkins-jnlp-agent'
            args: ['\$(JENKINS_SECRET)', '\$(JENKINS_NAME)']
            volumeMounts:
            - mountPath: /home/jenkins/.ssh
              name: volume-known-hosts
          volumes:
          - configMap:
              name: known-hosts
            name: volume-known-hosts
    '''
    }
  }
  
  parameters {
    choice(name: 'TARGET_PLATFORM', choices: ['oxygen', 'photon', 'r201809', 'r201812', 'latest'], description: 'Which Target Platform should be used?')
  }

  options {
    buildDiscarder(logRotator(numToKeepStr:'15'))
  }

  /*
  tools { 
    maven 'apache-maven-latest'
    jdk 'oracle-jdk8-latest'
  }
  */
  
  // https://jenkins.io/doc/book/pipeline/syntax/#triggers
  triggers {
    pollSCM('H/5 * * * *')
  }
  
  stages {
    stage('Checkout') {
      steps {
        script {
          properties([
            [$class: 'GithubProjectProperty', displayName: '', projectUrlStr: 'https://github.com/eclipse/xtext-core/'],
            [$class: 'RebuildSettings', autoRebuild: false, rebuildDisabled: false],
          ])
        }

        sh '''
          if [ -d ".git" ]; then
            git reset --hard
          fi
        '''
        
        checkout scm
        
        script {
          if ("latest" == params.TARGET_PLATFORM) {
            currentBuild.displayName = "#${BUILD_NUMBER}(4.11)"
          } else if ("r201812" == params.TARGET_PLATFORM) {
            currentBuild.displayName = "#${BUILD_NUMBER}(4.10)"
          } else if ("r201809" == params.TARGET_PLATFORM) {
            currentBuild.displayName = "#${BUILD_NUMBER}(4.9)"
          } else if ("photon" == params.TARGET_PLATFORM) {
            currentBuild.displayName = "#${BUILD_NUMBER}(4.8)"
          } else {
            currentBuild.displayName = "#${BUILD_NUMBER}(4.7)"
          }
        }

        dir('build') { deleteDir() }
        dir('.m2/repository/org/eclipse/xtext') { deleteDir() }
        dir('.m2/repository/org/eclipse/xtend') { deleteDir() }

        sh '''
          escaped() {
            echo $BRANCH_NAME | sed 's/\\//%252F/g'
          }
          
          escapedBranch=$(escaped)
          
          sed_inplace() {
            if [[ "$OSTYPE" == "darwin"* ]]; then
              sed -i '' "$@"
            else
              sed -i "$@" 
            fi  
          }
          
          targetfiles="$(find releng -type f -iname '*.target')"
          for targetfile in $targetfiles
          do
            echo "Redirecting target platforms in $targetfile to $BRANCH_NAME"
            sed_inplace "s?<repository location=\\".*/job/\\([^/]*\\)/job/[^/]*/?<repository location=\\"$JENKINS_URL/job/\\1/job/$escapedBranch/?" $targetfile
          done
        '''
      }
    }

    stage('Maven Build') {
      steps {
        container('container') {
          configFileProvider(
            [configFile(fileId: '7a78c736-d3f8-45e0-8e69-bf07c27b97ff', variable: 'MAVEN_SETTINGS')]) {
            sh '''
              if [ "latest" == "$target_platform" ] 
              then
                export targetProfile="-Platest"
              elif [ "r201809" == "$target_platform" ] 
              then
                export targetProfile="-Pr201809"
              elif [ "photon" == "$target_platform" ] 
              then
                export targetProfile="-Pphoton"
              else
                export targetProfile="-Poxygen"
              fi
              mvn \
                -s $MAVEN_SETTINGS \
                --batch-mode \
                -fae \
                -Dmaven.test.failure.ignore=true \
                -Dmaven.repo.local=.m2/repository \
                -DJENKINS_URL=$JENKINS_URL \
                -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn \
                $targetProfile \
                clean install
            '''
          }
        }
      }
    }
    
    /*
    stage('Integration Test') {
      steps {
        container('container') {
          configFileProvider(
            [configFile(fileId: '7a78c736-d3f8-45e0-8e69-bf07c27b97ff', variable: 'MAVEN_SETTINGS')]) {
            sh '''
              if [ "latest" == "$target_platform" ] 
              then
                export targetProfile="-Platest"
              elif [ "r201809" == "$target_platform" ] 
              then
                export targetProfile="-Pr201809"
              elif [ "photon" == "$target_platform" ] 
              then
                export targetProfile="-Pphoton"
              else
                export targetProfile="-Poxygen"
              fi
              mvn \
                -s $MAVEN_SETTINGS \
                --batch-mode \
                -fae \
                -Dmaven.test.failure.ignore=true \
                -Dmaven.repo.local=.m2/repository \
                -DJENKINS_URL=$JENKINS_URL \
                -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn \
                -pl \
                  org.eclipse.xtext.builder.tests,org.eclipse.xtext.eclipse.tests,org.eclipse.xtext.junit4.tests \
                $targetProfile \
                verify
            '''
          }
          // :org.eclipse.xtext.common.types.eclipse.tests,\ ,org.eclipse.xtext.eclipse.tests
//,\
//                  :org.eclipse.xtext.junit4.tests,\
//                  :org.eclipse.xtext.purexbase.eclipse.tests,\
//                  :org.eclipse.xtext.ui.codetemplates.tests,\
//                  :org.eclipse.xtext.ui.tests,\
//                  :org.eclipse.xtext.xbase.ui.tests,\
//                  :org.eclipse.xtext.xtext.ui.graph.tests,\
//                  :org.eclipse.xtext.xtext.ui.tests \          
        }
      }
    }
    */

    /*
    stage('Domainmodel Example') {
      steps {
        configFileProvider(
          [configFile(fileId: '7a78c736-d3f8-45e0-8e69-bf07c27b97ff', variable: 'MAVEN_SETTINGS')]) {
          sh '''
            mvn \
              -s $MAVEN_SETTINGS \
              -f org.eclipse.xtext.xtext.ui.examples/projects/domainmodel/org.eclipse.xtext.example.domainmodel.releng/pom.xml \
              --batch-mode \
              -fae \
              -DskipTests \
              -Dmaven.test.failure.ignore=true \
              -Dmaven.repo.local=.m2/repository \
              -DJENKINS_URL=$JENKINS_URL \
              -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn \
              clean verify
          '''
        }
      }
    }
    */
  }


  post {
    success {
      archiveArtifacts artifacts: 'build/**'
    }
    failure {
      archiveArtifacts artifacts: '**/target/work/data/.metadata/.log, **/hs_err_pid*.log'
    }
  }
}
