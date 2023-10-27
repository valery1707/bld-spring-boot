/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package rife.bld.extension;

import rife.bld.BuildCommand;
import rife.bld.Project;
import rife.bld.publish.PublishDeveloper;
import rife.bld.publish.PublishLicense;
import rife.bld.publish.PublishScm;

import java.util.List;

import static rife.bld.dependencies.Repository.*;
import static rife.bld.dependencies.Scope.compile;
import static rife.bld.dependencies.Scope.test;

public class SpringBootBuild extends Project {
    public SpringBootBuild() {
        pkg = "rife.bld.extension";
        name = "bld-spring-boot";
        version = version(0, 9, 0, "SNAPSHOT");

        javaRelease = 17;
        downloadSources = true;
        autoDownloadPurge = true;
        repositories = List.of(MAVEN_CENTRAL, RIFE2_RELEASES);

        scope(compile)
                .include(dependency("com.uwyn.rife2", "bld", version(1, 7, 5)));
        scope(test)
                .include(dependency("org.junit.jupiter", "junit-jupiter", version(5, 10, 0)))
                .include(dependency("org.junit.platform", "junit-platform-console-standalone", version(1, 10, 0)))
                .include(dependency("org.assertj", "assertj-core", version(3, 24, 2)));

        publishOperation()
                .repository(version.isSnapshot() ? RIFE2_SNAPSHOTS : RIFE2_RELEASES)
                .info()
                .groupId("com.uwyn.rife2")
                .artifactId("bld-spring-boot")
                .description("bld Extension for Spring Boot")
                .url("https://github.com/rife2/bld-spring-boot")
                .developer(new PublishDeveloper().id("ethauvin").name("Erik C. Thauvin").email("erik@thauvin.net")
                        .url("https://erik.thauvin.net/"))
                .license(new PublishLicense().name("The Apache License, Version 2.0")
                        .url("http://www.apache.org/licenses/LICENSE-2.0.txt"))
                .scm(new PublishScm().connection("scm:git:https://github.com/rife2/bld-spring-boot.git")
                        .developerConnection("scm:git:git@github.com:rife2/bld-spring-boot.git")
                        .url("https://github.com/rife2/bld-spring-boot"))
                .signKey(property("sign.key"))
                .signPassphrase(property("sign.passphrase"));
    }

    public static void main(String[] args) {
        new SpringBootBuild().start(args);
    }

    @BuildCommand(summary = "Runs PMD analysis")
    public void pmd() {
        new PmdOperation()
                .fromProject(this)
                .failOnViolation(true)
                .ruleSets("config/pmd.xml")
                .execute();
    }
}