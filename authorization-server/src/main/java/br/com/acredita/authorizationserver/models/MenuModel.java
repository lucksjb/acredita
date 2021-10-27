package br.com.acredita.authorizationserver.models;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class MenuModel {
	private String id;
	private String displayName;
	private boolean disabled;
	private boolean divider;
	private String iconName;
	private String route;
	private MenuModel father;
	private List<MenuModel> children;

	public MenuModel() {
	}

	public MenuModel(String id, String displayName, boolean disabled, boolean divider, String iconName, String route, MenuModel father, List<MenuModel> children) {
		super();
		this.id = id;
		this.displayName = displayName;
		this.disabled = disabled;
		this.divider = divider;
		this.iconName = iconName;
		this.route = route;
		this.father = father;
		this.children = children;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isDivider() {
		return divider;
	}

	public void setDivider(boolean divider) {
		this.divider = divider;
	}

	public String getIconName() {
		return iconName;
	}

	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	@JsonIgnore
	public MenuModel getFather() {
		return father;
	}

	public void setFather(MenuModel father) {
		this.father = father;
	}

	public List<MenuModel> getChildren() {
		return children;
	}

	public void setChildren(List<MenuModel> children) {
		this.children = children;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MenuModel other = (MenuModel) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE); // reflectionToString coloca um
	}

}
