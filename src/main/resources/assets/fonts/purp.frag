#extension GL_OES_standard_derivatives : disable

precision highp float;

uniform float time;
uniform vec2 mouse;
uniform vec2 resolution;


mat2 rotate2D(float r) {
    return mat2(cos(r), sin(r), -sin(r), cos(r));
}

// based on the follow tweet:
// https://twitter.com/zozuar/status/1621229990267310081
void main()
{
    // Normalized pixel coordinates (from 0 to 1)
    vec2 uv = (gl_FragCoord.xy-.5*resolution.xy)/resolution.y;
    vec3 col = vec3(0);
    float t = time;

    vec2 n = vec2(0);
    vec2 q = vec2(0);
    vec2 p = uv;
    float d = dot(p,p);
    float S = 12.;
    float a = 0.0;
    mat2 m = rotate2D(5.);

    for (float j = 0.; j < 20.; j++) {
        p *= m;
        n *= m;
        q = p * S + t * 4. + sin(t * 4. - d * 6.) * .8 + j + n; // wtf???
        a += dot(cos(q)/S, vec2(.2));
        n -= sin(q);
        S *= 1.2;
    }

    col = vec3(4, 2, 6) * (a + .2) + a + a - d;


    // Output to screen
    gl_FragColor = vec4(col,1.0);
}