package com.github.trecloux.yeoman;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;

/*
 * Copyright 2013 Thomas Recloux
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
@Mojo( name = "build", defaultPhase = LifecyclePhase.PREPARE_PACKAGE )
public class YeomanMojo extends AbstractMojo {
    @Parameter( defaultValue = "yo", required = true )
    File yeomanProjectDirectory;
    @Parameter( defaultValue = "${os.name}", readonly = true)
    String osName;
    @Parameter ( defaultValue = "--no-color" )
    String gruntArgs;

    public void execute() throws MojoExecutionException {
        npmInstall();
        bowerInstall();
        grunt();
    }

    private void npmInstall() throws MojoExecutionException {
        executeCommand("npm install");
    }
    private void bowerInstall() throws MojoExecutionException {
        executeCommand("bower install --no-color");
    }
    private void grunt() throws MojoExecutionException {
        executeCommand("grunt " + gruntArgs);
    }



    private void executeCommand(String command) throws MojoExecutionException {
        try {
            getLog().info("--------------------------------------");
            getLog().info("         " + command.toUpperCase());
            getLog().info("--------------------------------------");
            if (isWindows()) {
                command = "cmd /c " + command;
            }
            CommandLine cmdLine = CommandLine.parse(command);
            DefaultExecutor executor = new DefaultExecutor();
            executor.setWorkingDirectory(yeomanProjectDirectory);
            executor.execute(cmdLine);
        } catch (IOException e) {
            throw new MojoExecutionException("Error during : " + command, e);
        }
    }

    private boolean isWindows() {
        return osName.startsWith("Windows");
    }
}
