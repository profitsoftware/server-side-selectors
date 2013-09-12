Server Side Selectors
=====================

CSS style selectors for Vaadin on server side. Like "jQuery" for Vaadin.

Example: Hide all buttons with style 'cool'.
```
CSSSelectorParser parser = new CSSSelectorParser();
parser.query("button.cool").from(getUI()).hide();
```

Example: Run custom action on all components immediately inside HorizontalLayout.
```
CSSSelectorParser parser = new CSSSelectorParser();
parser.query("HorizontalLayout + *").from(getUI()).forEach(new QueryAction() {
	public void void action(Component component) {
		component.setWidth("100%");
	}
});
```
