package com.moekr.blog.web.flexmark;

import com.vladsch.flexmark.ast.Image;
import com.vladsch.flexmark.ast.util.TextCollectingVisitor;
import com.vladsch.flexmark.html.HtmlWriter;
import com.vladsch.flexmark.html.renderer.*;
import com.vladsch.flexmark.util.html.Attribute;
import com.vladsch.flexmark.util.html.Escaping;
import com.vladsch.flexmark.util.options.DataHolder;

import java.util.Set;

public class AmpCoreNodeRenderer extends CoreNodeRenderer {
    public AmpCoreNodeRenderer(DataHolder options) {
        super(options);
    }

    @Override
    public Set<NodeRenderingHandler<?>> getNodeRenderingHandlers() {
        Set<NodeRenderingHandler<?>> handlers = super.getNodeRenderingHandlers();
        handlers.removeIf(handler -> handler.getNodeType().equals(Image.class));
        handlers.add(new NodeRenderingHandler<>(Image.class, AmpCoreNodeRenderer.this::render));
        return handlers;
    }

    private void render(Image node, NodeRendererContext context, HtmlWriter html) {
        if (!context.isDoNotRenderLinks()) {
            String altText = new TextCollectingVisitor().collectAndGetText(node);
            ResolvedLink resolvedLink = context.resolveLink(LinkType.IMAGE, node.getUrl().unescape(), null, null);
            String url = resolvedLink.getUrl();

            if (!node.getUrlContent().isEmpty()) {
                // reverse URL encoding of =, &
                String content = Escaping.percentEncodeUrl(node.getUrlContent()).replace("+", "%2B").replace("%3D", "=").replace("%26", "&amp;");
                url += content;
            }

            html.attr("src", url);
            html.attr("alt", altText);
            html.attr("layout", "responsive");

            // we have a title part, use that
            if (node.getTitle().isNotNull()) {
                resolvedLink.getNonNullAttributes().replaceValue(Attribute.TITLE_ATTR, node.getTitle().unescape());
            } else {
                resolvedLink.getNonNullAttributes().remove(Attribute.TITLE_ATTR);
            }

            html.attr(resolvedLink.getAttributes());
            html.srcPos(node.getChars()).withAttr(resolvedLink).tagVoid("amp-img");
        }
    }
}
