package gov.state.ds.blobservice.utilities;

import static net.bramp.ffmpeg.FFmpeg.AUDIO_FORMAT_S16;
import static net.bramp.ffmpeg.FFmpeg.AUDIO_SAMPLE_48000;
import static net.bramp.ffmpeg.FFmpeg.FPS_30;
import static net.bramp.ffmpeg.builder.FFmpegBuilder.Verbosity;
import static net.bramp.ffmpeg.builder.MetadataSpecifier.*;
import static net.bramp.ffmpeg.builder.StreamSpecifier.tag;
import static net.bramp.ffmpeg.builder.StreamSpecifier.usable;
import static net.bramp.ffmpeg.builder.StreamSpecifierType.*;

import net.bramp.ffmpeg.builder.FFmpegBuilder;

public class VideoEncode {

	public static void main(String[] args) {
//		FFmpeg ffmpeg = new FFmpeg("/path/to/ffmpeg");
//		FFprobe ffprobe = new FFprobe("/path/to/ffprobe");
//
//		FFmpegBuilder builder = new FFmpegBuilder()
//
//		  .setInput("input.mp4")     // Filename, or a FFmpegProbeResult
//		  .overrideOutputFiles(true) // Override the output if it exists
//
//		  .addOutput("output.mp4")   // Filename for the destination
//		    .setFormat("mp4")        // Format is inferred from filename, or can be set
//		    .setTargetSize(250_000)  // Aim for a 250KB file
//
//		    .disableSubtitle()       // No subtiles
//
//		    .setAudioChannels(1)         // Mono audio
//		    .setAudioCodec("aac")        // using the aac codec
//		    .setAudioSampleRate(48_000)  // at 48KHz
//		    .setAudioBitRate(32768)      // at 32 kbit/s
//
//		    .setVideoCodec("libx264")     // Video using x264
//		    .setVideoFrameRate(24, 1)     // at 24 frames per second
//		    .setVideoResolution(640, 480) // at 640x480 resolution
//
//		    .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL); // Allow FFmpeg to use experimental specs
//
//		FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
//
//		// Run a one-pass encode
//		executor.createJob(builder).run();
//
//		// Or run a two-pass encode (which is better quality at the cost of being slower)
//		executor.createTwoPassJob(builder).run();	// TODO Auto-generated method stub

	}

}
