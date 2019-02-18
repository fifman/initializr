/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.initializr.app.controller;

import io.spring.initializr.app.generator.ExtendProjectRequest;
import io.spring.initializr.app.model.Archetype;
import io.spring.initializr.app.repo.ArchetypeRepository;
import io.spring.initializr.generator.BasicProjectRequest;
import io.spring.initializr.generator.ProjectGenerator;
import io.spring.initializr.generator.ProjectRequest;
import io.spring.initializr.metadata.DependencyMetadataProvider;
import io.spring.initializr.metadata.InitializrMetadataProvider;
import io.spring.initializr.util.TemplateRenderer;
import io.spring.initializr.web.project.MainController;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.ZipFileSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@Controller
public class ExtendMainController extends MainController {

	private static final Logger log = LoggerFactory.getLogger(MainController.class);

    private ArchetypeRepository archetypeRepository;

    private ProjectGenerator projectGenerator;

	public ExtendMainController(InitializrMetadataProvider metadataProvider, TemplateRenderer templateRenderer, ResourceUrlProvider resourceUrlProvider, ProjectGenerator projectGenerator, DependencyMetadataProvider dependencyMetadataProvider, ArchetypeRepository archetypeRepository) {
		super(metadataProvider, templateRenderer, resourceUrlProvider, projectGenerator, dependencyMetadataProvider);
		this.archetypeRepository = archetypeRepository;
		this.projectGenerator = projectGenerator;
	}

	private static String getWrapperScript(ProjectRequest request) {
		String script = "gradle".equals(request.getBuild()) ? "gradlew" : "mvnw";
		return request.getBaseDir() != null ? request.getBaseDir() + "/" + script
				: script;
	}

	private static String generateFileName(ProjectRequest request, String extension) {
		String tmp = request.getArtifactId().replaceAll(" ", "_");
		try {
			return URLEncoder.encode(tmp, "UTF-8") + "." + extension;
		}
		catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("Cannot encode URL", e);
		}
	}

	@ModelAttribute
	public ExtendProjectRequest extendProjectRequest(
			@RequestHeader Map<String, String> headers) {
		ExtendProjectRequest request = new ExtendProjectRequest();
		request.getParameters().putAll(headers);
		request.initialize(this.metadataProvider.get());
		return request;
	}

	@GetMapping(path = "/ui/archetypes", produces = "application/json")
    @ResponseBody
	public List<Archetype> archetypes() {
		return archetypeRepository.getArchetypeConfig().getArchetypes();
	}

	@RequestMapping("/starter.zip")
	@ResponseBody
	public ResponseEntity<byte[]> springZip(ExtendProjectRequest request)
			throws IOException {
		File dir = this.projectGenerator.generateProjectStructure(request);

		File download = this.projectGenerator.createDistributionFile(dir, ".zip");

		String wrapperScript = getWrapperScript(request);
		new File(dir, wrapperScript).setExecutable(true);
		Zip zip = new Zip();
		zip.setProject(new Project());
		zip.setDefaultexcludes(false);
		ZipFileSet set = new ZipFileSet();
		set.setDir(dir);
		set.setFileMode("755");
		set.setIncludes(wrapperScript);
		set.setDefaultexcludes(false);
		zip.addFileset(set);
		set = new ZipFileSet();
		set.setDir(dir);
		set.setIncludes("**,");
		set.setExcludes(wrapperScript);
		set.setDefaultexcludes(false);
		zip.addFileset(set);
		zip.setDestFile(download.getCanonicalFile());
		zip.execute();
		return upload(download, dir, generateFileName(request, "zip"), "application/zip");
	}

	@Override
    @RequestMapping("/start2.zip")
	public ResponseEntity<byte[]> springZip(BasicProjectRequest basicRequest) throws IOException {
		return super.springZip(basicRequest);
	}

	private ResponseEntity<byte[]> upload(File download, File dir, String fileName,
										  String contentType) throws IOException {
		byte[] bytes = StreamUtils.copyToByteArray(new FileInputStream(download));
		log.info("Uploading: {} ({} bytes)", download, bytes.length);
		ResponseEntity<byte[]> result = createResponseEntity(bytes, contentType,
				fileName);
		this.projectGenerator.cleanTempFiles(dir);
		return result;
	}

	private ResponseEntity<byte[]> createResponseEntity(byte[] content,
														String contentType, String fileName) {
		String contentDispositionValue = "attachment; filename=\"" + fileName + "\"";
		return ResponseEntity.ok().header("Content-Type", contentType)
				.header("Content-Disposition", contentDispositionValue).body(content);
	}

}
