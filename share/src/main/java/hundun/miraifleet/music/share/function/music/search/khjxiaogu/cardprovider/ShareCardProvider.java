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
import net.mamoe.mirai.message.data.RichMessage;

public class ShareCardProvider implements MusicCardProvider {

	public ShareCardProvider() {
	}

	@Override
	public Message process(MusicInfo mi, FunctionReplyReceiver receiver) {
		return RichMessage.share(mi.jurl.replaceAll("\\&", "&amp;"), mi.title, mi.desc,
				mi.purl.replaceAll("\\&", "&amp;"));
	}

}
