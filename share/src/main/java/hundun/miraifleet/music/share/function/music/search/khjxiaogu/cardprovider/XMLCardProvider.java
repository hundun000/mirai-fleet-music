/**
 * Mirai Song Plugin
 * Copyright (C) 2021  khjxiaogu
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package hundun.miraifleet.music.share.function.music.search.khjxiaogu.cardprovider;

import hundun.miraifleet.framework.core.function.FunctionReplyReceiver;
import hundun.miraifleet.music.share.function.music.search.khjxiaogu.MusicCardProvider;
import hundun.miraifleet.music.share.function.music.search.khjxiaogu.MusicInfo;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.SimpleServiceMessage;

public class XMLCardProvider implements MusicCardProvider {

	public XMLCardProvider() {
	}
	
	@Override
	public MessageChain process(MusicInfo mi, FunctionReplyReceiver receiver) {
		StringBuilder xmb = new StringBuilder("<msg serviceID=\"2\" templateID=\"1\" action=\"web\" actionData=\"\" a_actionData=\"\" i_actionData=\"\" brief=\"[音乐]")
				.append(escapeXmlTag(mi.title)).append("\" sourceMsgId=\"0\" url=\"").append(escapeXmlTag(mi.jurl))
				.append("\" flag=\"0\" adverSign=\"0\" multiMsgFlag=\"0\">\r\n<item layout=\"2\">\r\n")
				.append("<audio cover=\"").append(escapeXmlTag(mi.purl)).append("\" src=\"")
				.append(escapeXmlTag(mi.murl)).append("\"/>\r\n").append("<title>").append(escapeXmlContent(mi.title))
				.append("</title>\r\n<summary>").append(escapeXmlContent(mi.desc))
				.append("</summary>\r\n</item>\r\n<source name=\"").append(escapeXmlTag(mi.source)).append("\" icon=\"")
				.append(escapeXmlTag(mi.icon))
				.append("\" url=\"\" action=\"web\" a_actionData=\"tencent0://\" i_actionData=\"\" appid=\"").append(mi.appid)
				.append("\"/>\r\n</msg>");
		Message msg = new SimpleServiceMessage(2, xmb.toString());
		return msg.plus(mi.jurl);
	}

	public String escapeXmlContent(String org) {
		return org.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}

	public String escapeXmlTag(String org) {
		return org.replaceAll("\\&", "&amp;").replaceAll("\"", "&quot;").replaceAll("'", "&apos;")
				.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}
}
