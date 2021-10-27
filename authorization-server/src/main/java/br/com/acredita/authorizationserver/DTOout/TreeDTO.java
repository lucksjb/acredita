package br.com.acredita.authorizationserver.DTOout;

import java.util.ArrayList;
import java.util.List;

import br.com.acredita.authorizationserver.models.Programa;

public class TreeDTO {
	private String label;
	private String data;
	private String expandedIcon;
	private String collapsedIcon;
	private boolean expanded;
	private List<TreeDTO> children;

	public TreeDTO() {
	}

	public TreeDTO(String label, String data, String expandedIcon, String collapsedIcon, boolean expanded,
			List<TreeDTO> children) {
		super();
		this.label = label;
		this.data = data;
		this.expandedIcon = expandedIcon;
		this.collapsedIcon = collapsedIcon;
		this.expanded = expanded;
		this.children = children;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getExpandedIcon() {
		return expandedIcon;
	}

	public void setExpandedIcon(String expandedIcon) {
		this.expandedIcon = expandedIcon;
	}

	public String getCollapsedIcon() {
		return collapsedIcon;
	}

	public void setCollapsedIcon(String collapsedIcon) {
		this.collapsedIcon = collapsedIcon;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public List<TreeDTO> getChildren() {
		return children;
	}

	public void setChildren(List<TreeDTO> children) {
		this.children = children;
	}

	public static List<TreeDTO> criaTreeDTORecursivo(Programa programa) {
		String label = programa.getDescricao();
		String data = programa.getId().toString();
		String expandedIcon = "fa fa-folder-open";
		String collapsedIcon = "fa fa-folder";
		boolean expanded = true;
		List<TreeDTO> resultado = new ArrayList<>();
		if (!programa.getSubMenu().isEmpty()) {
			for (Programa p : programa.getSubMenu()) {
				if (!p.getSubMenu().isEmpty()) {
					label = p.getDescricao();
					data = p.getId().toString();
					resultado.add(
							new TreeDTO(label, data, expandedIcon, collapsedIcon, expanded, criaTreeDTORecursivo(p)));
				}
			}
		} else {
			resultado.add(new TreeDTO(label, data, expandedIcon, collapsedIcon, expanded, resultado));
		}
		return resultado;
	}

	@Override
	public String toString() {

		return this.label + "-" + this.data + " - " + this.children;
	}

}
