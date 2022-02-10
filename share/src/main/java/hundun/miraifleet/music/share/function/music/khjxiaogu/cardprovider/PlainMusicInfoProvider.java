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
package hundun.miraifleet.music.share.function.music.khjxiaogu.cardprovider;

import java.io.IOException;
import java.net.MalformedURLException;

import hundun.miraifleet.framework.core.function.FunctionReplyReceiver;
import hundun.miraifleet.music.share.function.music.khjxiaogu.MusicCardProvider;
import hundun.miraifleet.music.share.function.music.khjxiaogu.MusicInfo;
import hundun.miraifleet.music.share.function.music.khjxiaogu.Utils;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;
import net.mamoe.mirai.utils.OverFileSizeMaxException;

public class PlainMusicInfoProvider implements MusicCardProvider {

	public PlainMusicInfoProvider() {
	}

	@Override
	public Message process(MusicInfo mi, FunctionReplyReceiver receiver) throws OverFileSizeMaxException, MalformedURLException {
		Message im = null;
		try(ExternalResource ex=ExternalResource.create(Utils.getFromHttp(mi.purl))){
			im = receiver.uploadImageOrNotSupportPlaceholder(ex);
		} catch (IOException e) {
		}
		StringBuilder tsb = new StringBuilder().append("歌名：").append(mi.title).append("\n作者：").append(mi.desc)
				.append("\n封面：").append(mi.purl).append("\n外链：").append(mi.murl).append("\n链接：").append(mi.jurl)
				.append("\n来自：").append(mi.source);
		if (im != null)
			return im.plus(tsb.toString());
		return new PlainText(tsb.toString());
	}

}
