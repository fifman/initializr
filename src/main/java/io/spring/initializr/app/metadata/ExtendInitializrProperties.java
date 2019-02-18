package io.spring.initializr.app.metadata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.spring.initializr.metadata.*;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ExtendInitializrProperties extends InitializrConfiguration {

	/**
	 * Dependencies, organized in groups (i.e. themes).
	 */
	@JsonIgnore
	private List<ExtendDependencyGroup> dependencies = new ArrayList<>();

	/**
	 * Available project types.
	 */
	@JsonIgnore
	private List<Type> types = new ArrayList<>();

	/**
	 * Available packaging types.
	 */
	@JsonIgnore
	private List<DefaultMetadataElement> packagings = new ArrayList<>();

	/**
	 * Available java versions.
	 */
	@JsonIgnore
	private List<DefaultMetadataElement> javaVersions = new ArrayList<>();

	/**
	 * Available programming languages.
	 */
	@JsonIgnore
	private List<DefaultMetadataElement> languages = new ArrayList<>();

	/**
	 * Available Spring Boot versions.
	 */
	@JsonIgnore
	private List<DefaultMetadataElement> bootVersions = new ArrayList<>();

	/**
	 * GroupId metadata.
	 */
	@JsonIgnore
	private SimpleElement groupId = new SimpleElement("com.example");

	/**
	 * ArtifactId metadata.
	 */
	@JsonIgnore
	private SimpleElement artifactId = new SimpleElement(null);

	/**
	 * Version metadata.
	 */
	@JsonIgnore
	private SimpleElement version = new SimpleElement("0.0.1-SNAPSHOT");

	/**
	 * Name metadata.
	 */
	@JsonIgnore
	private SimpleElement name = new SimpleElement("demo");

	/**
	 * Description metadata.
	 */
	@JsonIgnore
	private SimpleElement description = new SimpleElement(
			"Demo project for Spring Boot");

	/**
	 * Package name metadata.
	 */
	@JsonIgnore
	private SimpleElement packageName = new SimpleElement(null);

	public List<ExtendDependencyGroup> getDependencies() {
		return this.dependencies;
	}

	public List<Type> getTypes() {
		return this.types;
	}

	public List<DefaultMetadataElement> getPackagings() {
		return this.packagings;
	}

	public List<DefaultMetadataElement> getJavaVersions() {
		return this.javaVersions;
	}

	public List<DefaultMetadataElement> getLanguages() {
		return this.languages;
	}

	public List<DefaultMetadataElement> getBootVersions() {
		return this.bootVersions;
	}

	public void setDependencies(List<ExtendDependencyGroup> dependencies) {
		this.dependencies = dependencies;
	}

	public void setTypes(List<Type> types) {
		this.types = types;
	}

	public void setPackagings(List<DefaultMetadataElement> packagings) {
		this.packagings = packagings;
	}

	public void setJavaVersions(List<DefaultMetadataElement> javaVersions) {
		this.javaVersions = javaVersions;
	}

	public void setLanguages(List<DefaultMetadataElement> languages) {
		this.languages = languages;
	}

	public void setBootVersions(List<DefaultMetadataElement> bootVersions) {
		this.bootVersions = bootVersions;
	}

	public void setGroupId(SimpleElement groupId) {
		this.groupId = groupId;
	}

	public void setArtifactId(SimpleElement artifactId) {
		this.artifactId = artifactId;
	}

	public void setVersion(SimpleElement version) {
		this.version = version;
	}

	public void setName(SimpleElement name) {
		this.name = name;
	}

	public void setDescription(SimpleElement description) {
		this.description = description;
	}

	public void setPackageName(SimpleElement packageName) {
		this.packageName = packageName;
	}

	/**
	 * A simple element from the properties.
	 */
	public static class SimpleElement {

		/**
		 * Element title.
		 */
		private String title;

		/**
		 * Element description.
		 */
		private String description;

		/**
		 * Element default value.
		 */
		private String value;

		public SimpleElement(String value) {
			this.value = value;
		}

		public String getTitle() {
			return this.title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getDescription() {
			return this.description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getValue() {
			return this.value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public void apply(TextCapability capability) {
			if (StringUtils.hasText(this.title)) {
				capability.setTitle(this.title);
			}
			if (StringUtils.hasText(this.description)) {
				capability.setDescription(this.description);
			}
			if (StringUtils.hasText(this.value)) {
				capability.setContent(this.value);
			}
		}

	}

}
